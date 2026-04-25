import { useContext, useEffect, useMemo, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import Card from "../components/common/Card";
import { useAuth } from "../hooks/useAuth";
import { useStaff } from "../hooks/queries/useStaff";
import { useBookingsByStaff } from "../hooks/queries/useBookingsByStaff";
import { UIStateContext } from "../context/UIStateContext";
import { useUpdateBookingStatus } from "../hooks/mutations/useUpdateBookingStatus";

function StaffMyBookingsPage() {
    const { showError, showSuccess } = useContext(UIStateContext);
    const { user } = useAuth();
    const roleNameRaw = typeof user?.role === 'string' ? user.role : user?.role?.name;
    const roleNameUpper = roleNameRaw ? String(roleNameRaw).toUpperCase() : null;
    
    const queryClient = useQueryClient();
    const futureStatuses = ["BOOKED", "NO_SHOW", "CANCELLED"];
    const pastStatuses = ["COMPLETED", "NO_SHOW", "CANCELLED"];
    const autoCompletedIdsRef = useRef(new Set());

    const { data: staff = [] } = useStaff();

    const currentStaff = useMemo(
        () => staff.find((member) => member.user?.userId === user?.userId),
        [staff, user]
    );

    const staffId = currentStaff?.staffId;

    const {
        data: bookings = [],
        isLoading: loadingBookings,
        error: bookingsQueryError,
    } = useBookingsByStaff(staffId);

    const [updatingBookingId, setUpdatingBookingId] = useState(null);

    const bookingsError = bookingsQueryError
        ? bookingsQueryError?.response?.data?.detail ||
        bookingsQueryError?.response?.data?.message ||
        bookingsQueryError?.message ||
        "Failed to load bookings."
        : null;

    const updateBookingStatusMutation = useUpdateBookingStatus();

    const sortedBookings = Array.isArray(bookings)
        ? [...bookings].sort((firstBooking, secondBooking) => {
            const firstBooked = String(firstBooking.status || "").toUpperCase() === "BOOKED";
            const secondBooked = String(secondBooking.status || "").toUpperCase() === "BOOKED";
            if (firstBooked !== secondBooked) {
                return secondBooked - firstBooked;
            }

            const firstStart = firstBooking.startDateTime
                ? new Date(firstBooking.startDateTime).getTime()
                : Number.NEGATIVE_INFINITY;

            const secondStart = secondBooking.startDateTime
                ? new Date(secondBooking.startDateTime).getTime()
                : Number.NEGATIVE_INFINITY;

            return secondStart - firstStart;
        })
        : [];

    const formatDisplayDate = (isoDateTime) => {
        if (!isoDateTime) return "Unknown";

        const parsedDate = new Date(isoDateTime);
        if (Number.isNaN(parsedDate.getTime())) return "Unknown";

        const year = parsedDate.getFullYear();
        const month = String(parsedDate.getMonth() + 1).padStart(2, "0");
        const day = String(parsedDate.getDate()).padStart(2, "0");
        const hours = String(parsedDate.getHours()).padStart(2, "0");
        const minutes = String(parsedDate.getMinutes()).padStart(2, "0");

        return `${year}.${month}.${day} ${hours}:${minutes}`;
    };

    const changeBookingStatus = async (booking, nextStatus) => {
        const bookingId = booking.bookingId ?? booking.id
        if (!bookingId || !nextStatus) {
            showError("Missing booking or status.");
            return;
        }

        const startsAt = booking.startDateTime ? new Date(booking.startDateTime).getTime() : null;
        const isPastBooking = startsAt != null && startsAt < Date.now();
        if (isPastBooking && nextStatus === "BOOKED") {
            showError("Past bookings cannot be set to BOOKED.");
            return;
        }

        const currentStatus = String(booking.status || "").toUpperCase();
        if (currentStatus === nextStatus) return;

        const confirmed = window.confirm(`Are you sure you want to set this booking to ${nextStatus}?`);
        if (!confirmed) return;

        try {
            setUpdatingBookingId(bookingId);

            const updateBody = {
                startDateTime: booking.startDateTime,
                endDateTime: booking.endDateTime,
                serviceId: booking.service?.serviceId,
                status: nextStatus
            }
            await updateBookingStatusMutation.mutateAsync({
                bookingId,
                bookingUpdateBody: updateBody, 
                isStaff: roleNameUpper === "STAFF" || roleNameUpper === "ADMIN"
            });

            await queryClient.invalidateQueries({ queryKey: ["staffBookings", staffId] });
            await queryClient.invalidateQueries({ queryKey: ["bookingsByStaff", staffId] });
            await queryClient.invalidateQueries({ queryKey: ["bookings", staffId] });

            showSuccess(`Booking status updated to ${nextStatus}.`);
        } catch (error) {
            const message =
                error?.response?.data?.detail ||
                error?.response?.data?.message ||
                error?.message ||
                "Failed to update booking status.";

            showError(message);
        } finally {
            setUpdatingBookingId(null);
        }
    };

    useEffect(() => {
        if (bookingsError) {
            showError(bookingsError);
        }
    }, [bookingsError, showError]);

    useEffect(() => {
        const now = Date.now();
        const pastBooked = (Array.isArray(bookings) ? bookings : []).filter((booking) => {
            const bookingId = booking.bookingId ?? booking.id;
            if (!bookingId || autoCompletedIdsRef.current.has(bookingId)) return false;
            const startsAt = booking.startDateTime ? new Date(booking.startDateTime).getTime() : null;
            const isPastBooking = startsAt != null && startsAt < now;
            const isBooked = String(booking.status || "").toUpperCase() === "BOOKED";
            return isPastBooking && isBooked;
        });

        if (pastBooked.length === 0) return;

        (async () => {
            try {
                for (const booking of pastBooked) {
                    const bookingId = booking.bookingId ?? booking.id;
                    autoCompletedIdsRef.current.add(bookingId);
                    const updateBody = {
                        startDateTime: booking.startDateTime,
                        endDateTime: booking.endDateTime,
                        serviceId: booking.service?.serviceId,
                        status: "COMPLETED",
                    };
                    await updateBookingStatusMutation.mutateAsync({
                        bookingId,
                        bookingUpdateBody: updateBody,
                        isStaff: roleNameUpper === "STAFF" || roleNameUpper === "ADMIN"
                    });
                }

                await queryClient.invalidateQueries({ queryKey: ["staffBookings", staffId] });
                await queryClient.invalidateQueries({ queryKey: ["bookingsByStaff", staffId] });
                await queryClient.invalidateQueries({ queryKey: ["bookings", staffId] });
            } catch (error) {
                showError(
                    error?.response?.data?.detail ||
                    error?.response?.data?.message ||
                    error?.message ||
                    "Failed to auto-complete past bookings."
                );
            }
        })();
    }, [bookings, staffId, updateBookingStatusMutation, queryClient, showError]);

    if (!user) {
        return (
            <section className="section">
                <div className="container">
                    <Card>
                        <div className="card-body">
                            <h2>Not signed in</h2>
                            <p className="muted">
                                Please <Link to="/login">log in</Link> to see your bookings.
                            </p>
                        </div>
                    </Card>
                </div>
            </section>
        );
    }

    if (roleNameUpper !== "STAFF" && roleNameUpper !== "ADMIN") {
        return (
            <section className="section">
                <div className="container">
                    <Card>
                        <div className="card-body">
                            <h2>Access denied</h2>
                            <p className="muted">You do not have permission to view staff bookings.</p>
                        </div>
                    </Card>
                </div>
            </section>
        );
    }

    return (
        <section className="section profile-section">
            <div className="container">
                <div className="page profile-page">
                    <div className="profile-grid" style={{ gridTemplateColumns: "1fr" }}>
                        <Card>
                            <div className="card-body">
                                <h3 style={{ marginTop: 0 }}>My customer bookings</h3>
                                <p className="muted">
                                    Review your upcoming and past appointments, then update their status when needed.
                                </p>

                                {loadingBookings && <p className="muted">Loading bookings...</p>}

                                {!loadingBookings && sortedBookings.length === 0 && !bookingsError && (
                                    <p className="muted">You have no customer bookings.</p>
                                )}

                                {!loadingBookings && sortedBookings.length > 0 && (
                                    <div className="booking-list">
                                        {sortedBookings.map((booking, index) => {
                                            const bookingId = booking.bookingId ?? booking.id;
                                            const normalizedStatus = String(booking.status || "").toUpperCase();
                                            const startsAt = booking.startDateTime ? new Date(booking.startDateTime).getTime() : null;
                                            const isPastBooking = startsAt != null && startsAt < Date.now();
                                            const allowedStatuses = isPastBooking ? pastStatuses : futureStatuses;
                                            const statusClass = booking.status
                                                ? `status-badge ${String(booking.status).toLowerCase().replace(/\s+/g, "_")}`
                                                : "status-badge";
                                            const selectedStatus = allowedStatuses.includes(normalizedStatus)
                                                ? normalizedStatus
                                                : (isPastBooking ? "COMPLETED" : "BOOKED");

                                            return (
                                                <div key={bookingId ?? `staff-booking-${index}`} className="booking-item">
                                                    <div
                                                        style={{
                                                            display: "flex",
                                                            justifyContent: "space-between",
                                                            gap: 12,
                                                            width: "100%",
                                                        }}
                                                    >
                                                        <div>
                                                            <strong>
                                                                {booking.startDateTime
                                                                    ? formatDisplayDate(booking.startDateTime)
                                                                    : "Unknown"}
                                                            </strong>

                                                            <div className="muted">
                                                                Customer: {booking.user?.user?.name || booking.user?.name || "—"}
                                                            </div>

                                                            <div className="muted">
                                                                Customer phone:{" "}
                                                                {booking.user?.user?.phoneNumber ||
                                                                    booking.user?.phoneNumber ||
                                                                    "—"}
                                                            </div>

                                                            <div className="muted">
                                                                Service:{" "}
                                                                {booking.service?.name
                                                                    ? `${booking.service.name}${booking.service?.price != null
                                                                        ? ` | ${booking.service.price} Ft`
                                                                        : ""
                                                                    }`
                                                                    : "—"}
                                                            </div>
                                                        </div>

                                                        <div
                                                            style={{
                                                                alignSelf: "center",
                                                                display: "flex",
                                                                flexDirection: "column",
                                                                alignItems: "flex-end",
                                                                gap: 8,
                                                            }}
                                                        >
                                                            <span className={statusClass}>{booking.status || "Unknown"}</span>

                                                            <div className="booking-status-control">
                                                                <select
                                                                    className="booking-status-select"
                                                                    value={selectedStatus}
                                                                    disabled={
                                                                        updateBookingStatusMutation.isPending &&
                                                                        updatingBookingId === bookingId
                                                                    }
                                                                    onChange={(e) => changeBookingStatus(booking, e.target.value)}
                                                                >
                                                                    {allowedStatuses.map((statusOption) => (
                                                                        <option key={statusOption} value={statusOption}>
                                                                            {statusOption.replace("_", " ")}
                                                                        </option>
                                                                    ))}
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            );
                                        })}
                                    </div>
                                )}
                            </div>
                        </Card>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default StaffMyBookingsPage;
