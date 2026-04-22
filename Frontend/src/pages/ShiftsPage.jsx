import { Link } from "react-router-dom";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import { useShiftsByStaff } from "../hooks/queries/useShiftsByStaff";
import { useStaff } from "../hooks/queries/useStaff";
import { useAuth } from "../hooks/useAuth";
import Button from "../components/common/Button";
import { useUpdateShiftForStaffDay } from "../hooks/mutations/useUpdateShiftForStaffDay";
import { useEffect, useState, useContext } from "react";
import { useDeleteStaffShift } from "../hooks/mutations/useDeleteStaffShift";
import { UIStateContext } from "../context/UIStateContext.jsx";

const WEEK_SHIFT_TEMPLATE = [
    { shiftId: "monday", day: "MONDAY", startShift: "", endShift: "" },
    { shiftId: "tuesday", day: "TUESDAY", startShift: "", endShift: "" },
    { shiftId: "wednesday", day: "WEDNESDAY", startShift: "", endShift: "" },
    { shiftId: "thursday", day: "THURSDAY", startShift: "", endShift: "" },
    { shiftId: "friday", day: "FRIDAY", startShift: "", endShift: "" },
    { shiftId: "saturday", day: "SATURDAY", startShift: "", endShift: "" },
    { shiftId: "sunday", day: "SUNDAY", startShift: "", endShift: "" },
];

export default function ShiftsPage() {
    const { showSuccess, showError, showLoading, getErrorMessage, hideNotification } = useContext(UIStateContext);

    const { user, staff } = useAuth();
    const roleNameUpper = user?.role?.name ? String(user.role.name).toUpperCase() : null;
    const { mutate: updateShiftMutate } = useUpdateShiftForStaffDay();
    const { mutate: removeShiftMutate } = useDeleteStaffShift();

    const { data: shifts = [], isLoading, error: shiftError } = useShiftsByStaff(staff?.staffId);

    const [shiftDrafts, setShiftDrafts] = useState({});


    useEffect(() => {
        if (roleNameUpper !== "STAFF" && roleNameUpper !== "ADMIN") return;

        setShiftDrafts((currentDrafts) => {
            const nextDrafts = {};

            shifts.forEach((shift) => {
                nextDrafts[shift.shiftId] = {
                    startShift: shift.startShift,
                    endShift: shift.endShift,
                };
            });

            const currentKeys = Object.keys(currentDrafts);
            const nextKeys = Object.keys(nextDrafts);

            if (currentKeys.length !== nextKeys.length) return nextDrafts;

            for (const key of nextKeys) {
                if (
                    currentDrafts[key]?.startShift !== nextDrafts[key].startShift ||
                    currentDrafts[key]?.endShift !== nextDrafts[key].endShift
                ) {
                    return nextDrafts;
                }
            }

            return currentDrafts;
        });
    }, [shifts, roleNameUpper]);

    const [orderedShifts, setOrderedShifts] = useState(WEEK_SHIFT_TEMPLATE);

    useEffect(() => {
        const newOrderedShifts = WEEK_SHIFT_TEMPLATE.map((shift) => ({ ...shift }));
        shifts.forEach(shift => {
            // Find index by day and update
            const index = newOrderedShifts.findIndex(s => s.day === shift.day);
            if (index !== -1) newOrderedShifts[index] = shift;
        });
        setOrderedShifts(newOrderedShifts);
    }, [shifts]);

    if (roleNameUpper !== "STAFF" && roleNameUpper !== "ADMIN") return (
        <section className="section">
            <div className="container">
                <p>You do not have permission to view this page.</p>
            </div>
        </section>
    );

    if (isLoading) {
        return (
            <section className="section">
                <div className="container">
                    <p>Loading shifts...</p>
                </div>
            </section>
        );
    }

    if (shiftError) {
        showError(getErrorMessage(shiftError));
        return (
            <section className="section">
                <div className="container">
                    <p>Failed to load shifts. Please try again later.</p>
                </div>
            </section>
        );
    }

    const updateShift = async (staffId, shift) => {
        const draft = shiftDrafts[shift.shiftId] ?? shift;

        showLoading("Saving shift...");

        await updateShiftMutate(
            {
                staffId,
                day: shift.day,
                startShift: draft.startShift,
                endShift: draft.endShift,
            },
            {
                onSuccess: () => {
                    showSuccess("Shift updated successfully!");
                },
                onError: (err) => {
                    showError(getErrorMessage(err));
                },
            }
        );
    };

    const resetShift = (shift) => {
        setShiftDrafts((currentDrafts) => {
            const updated = { ...currentDrafts };
            delete updated[shift.shiftId];
            return updated;
        });
    };

    const removeShift = async (staffId, shiftId) => {
        if (!confirm("Are you sure you want to remove this shift?", "Confirm Removal")) return;

        showLoading("Removing shift...");

        await removeShiftMutate(
            { staffId, shiftId },
            {
                onSuccess: () => {
                    showSuccess("Shift removed successfully!");
                    setShiftDrafts((currentDrafts) => {
                        const updated = { ...currentDrafts };
                        delete updated[shiftId];
                        return updated;
                    });
                },
                onError: (err) => {
                    showError(getErrorMessage(err));
                },
            }
        );
    };

    const isShiftModified = (shift) => {
        const draft = shiftDrafts[shift.shiftId];
        if (!draft) return false;
        return draft.startShift !== shift.startShift || draft.endShift !== shift.endShift;
    }

    return (
        <section className="section shift-page">
            <div className="container">
                <div className="shift-hero">
                    <SectionHeader
                        eyebrow="My Shifts"
                        title="Manage your weekly availability"
                        description="Update your working hours for each day of the week in a clean, focused schedule view."
                    />

                    <div className="shift-hero-note">
                        <span className="status-dot" />
                        Changes are saved per day. Only modified shifts can be updated.
                    </div>
                </div>

                <div className="shift-grid">
                    {orderedShifts.map((shift) => {

                        const isModified = isShiftModified(shift);

                        return (
                            <Card
                                key={shift.shiftId ?? `day-${shift.day}`}
                                className={`shift-card ${isModified ? "shift-card-modified" : ""}`}
                            >
                                <div className="card-body shift-card-body">
                                    <div className="shift-card-top">
                                        <div>
                                            <p className="shift-label">Working day</p>
                                            <h3 className="shift-day">
                                                {shift.day.charAt(0) + shift.day.slice(1).toLowerCase()}
                                            </h3>
                                        </div>

                                        <div className={`shift-status ${isModified ? "modified" : "synced"}`}>
                                            {isModified ? "Unsaved changes" : "Saved"}
                                        </div>
                                    </div>

                                    <div className="shift-time-grid">
                                        <div className="shift-field">
                                            <label htmlFor={`start-${shift.shiftId}`}>Start time</label>
                                            <input
                                                id={`start-${shift.shiftId}`}
                                                className="shift-time-input"
                                                type="time"
                                                value={shiftDrafts[shift.shiftId]?.startShift ?? shift.startShift}
                                                onChange={(event) =>
                                                    setShiftDrafts((currentDrafts) => ({
                                                        ...currentDrafts,
                                                        [shift.shiftId]: {
                                                            ...currentDrafts[shift.shiftId],
                                                            startShift: event.target.value,
                                                        },
                                                    }))
                                                }
                                            />
                                        </div>

                                        <div className="shift-time-separator">→</div>

                                        <div className="shift-field">
                                            <label htmlFor={`end-${shift.shiftId}`}>End time</label>
                                            <input
                                                id={`end-${shift.shiftId}`}
                                                className="shift-time-input"
                                                type="time"
                                                value={shiftDrafts[shift.shiftId]?.endShift ?? shift.endShift}
                                                onChange={(event) =>
                                                    setShiftDrafts((currentDrafts) => ({
                                                        ...currentDrafts,
                                                        [shift.shiftId]: {
                                                            ...currentDrafts[shift.shiftId],
                                                            endShift: event.target.value,
                                                        },
                                                    }))
                                                }
                                            />
                                        </div>
                                    </div>

                                    <div className="shift-card-footer">
                                        <div className="shift-summary">
                                            <span className="shift-summary-label">Current schedule</span>
                                            <strong>
                                                {(shiftDrafts[shift.shiftId]?.startShift ?? shift.startShift)} -{" "}
                                                {(shiftDrafts[shift.shiftId]?.endShift ?? shift.endShift)}
                                            </strong>
                                        </div>

                                        <div className="shift-actions">
                                            <Button
                                                disabled={!isModified}
                                                className="btn btn-primary shift-save-btn"
                                                onClick={() => updateShift(staff?.staffId, shift)}
                                            >
                                                {isModified ? "Save changes" : "Up to date"}
                                            </Button>

                                            <Button
                                                className="btn shift-reset-btn"
                                                onClick={() => resetShift(shift)}
                                            >
                                                Reset
                                            </Button>

                                            <Button
                                                className="remove-btn"
                                                onClick={() => removeShift(staff?.staffId, shift?.shiftId)}
                                            >
                                                Remove
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        );
                    })}
                </div>
            </div>
        </section>
    );
}