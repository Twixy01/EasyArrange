import { useContext, useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import { useAuth } from "../hooks/useAuth";
import { useStaff } from "../hooks/queries/useStaff";
import { useBookingsByStaff } from "../hooks/queries/useBookingsByStaff";
import { UIStateContext } from "../context/UIStateContext";
import { useUpdateBookingStatus } from "../hooks/mutations/useUpdateBookingStatus";

function StaffBookingsPage() {
    const { showError, showSuccess } = useContext(UIStateContext);
    const { user } = useAuth();
    const queryClient = useQueryClient();

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
        const bookingId = booking.bookingId
        if (!bookingId || !nextStatus) {
            showError("Missing booking or status.");
            return;
        }

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
            await updateBookingStatusMutation.mutateAsync({ bookingId, bookingUpdateBody: updateBody });

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

    if (user?.role?.name !== "STAFF" && user?.role?.name !== "ADMIN") {
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
                                            const statusClass = booking.status
                                                ? `status-badge ${String(booking.status).toLowerCase().replace(/\s+/g, "_")}`
                                                : "status-badge";

                                            const canMarkNoShow = normalizedStatus === "BOOKED";
                                            const canCancel = normalizedStatus === "BOOKED";

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
                                                                Customer: {booking.customer?.user?.name || booking.customer?.name || "—"}
                                                            </div>

                                                            <div className="muted">
                                                                Customer phone:{" "}
                                                                {booking.customer?.user?.phoneNumber ||
                                                                    booking.customer?.phoneNumber ||
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

                                                            {canMarkNoShow && (
                                                                <Button
                                                                    className="btn-secondary"
                                                                    disabled={
                                                                        updateBookingStatusMutation.isPending &&
                                                                        updatingBookingId === bookingId
                                                                    }
                                                                    onClick={() => changeBookingStatus(booking, "NO_SHOW")}
                                                                >
                                                                    {updateBookingStatusMutation.isPending &&
                                                                        updatingBookingId === bookingId
                                                                        ? "Updating..."
                                                                        : "Mark no-show"}
                                                                </Button>
                                                            )}

                                                            {canCancel && (
                                                                <Button
                                                                    className="remove-btn"
                                                                    disabled={
                                                                        updateBookingStatusMutation.isPending &&
                                                                        updatingBookingId === bookingId
                                                                    }
                                                                    onClick={() => changeBookingStatus(booking, "CANCELLED")}
                                                                >
                                                                    {updateBookingStatusMutation.isPending &&
                                                                        updatingBookingId === bookingId
                                                                        ? "Updating..."
                                                                        : "Cancel booking"}
                                                                </Button>
                                                            )}
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

export default StaffBookingsPage;
