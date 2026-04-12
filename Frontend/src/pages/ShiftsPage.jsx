import { Link } from "react-router-dom";
import SectionHeader from "../components/common/SectionHeader";
import Card from "../components/common/Card";
import { useShiftsByStaff } from "../hooks/queries/useShiftsByStaff";
import { useStaff } from "../hooks/queries/useStaff";
import { useAuth } from "../hooks/useAuth";
import Button from "../components/common/Button";
import { useUpdateShiftForStaffDay } from "../hooks/mutations/useUpdateShiftForStaffDay";
import { useEffect, useState } from "react";

export default function ShiftsPage() {
    const { user } = useAuth();
    const { data: staff } = useStaff();

    const currentStaff = staff?.find(s => s.user?.userId === user?.userId);
    if (user?.role.name !== "STAFF" && user?.role.name !== "ADMIN") return (<p>You do not have permission to view this page.</p>);

    const { data: shifts = [], isLoading, error: shiftError } = useShiftsByStaff(currentStaff?.staffId);

    const { mutate: updateShiftMutate } = useUpdateShiftForStaffDay();

    const [success, setSuccess] = useState("");
    const [error, setError] = useState("");

    const [shiftDrafts, setShiftDrafts] = useState({});

    useEffect(() => {
        const nextDrafts = {};

        shifts.forEach((shift) => {
            nextDrafts[shift.shiftId] = {
                startShift: shift.startShift,
                endShift: shift.endShift,
            };
        });

        setShiftDrafts(nextDrafts);
    }, [shifts]);

    const orderedShifts = [
        { day: "MONDAY", startShift: "", endShift: "" },
        { day: "TUESDAY", startShift: "", endShift: "" },
        { day: "WEDNESDAY", startShift: "", endShift: "" },
        { day: "THURSDAY", startShift: "", endShift: "" },
        { day: "FRIDAY", startShift: "", endShift: "" },
        { day: "SATURDAY", startShift: "", endShift: "" },
        { day: "SUNDAY", startShift: "", endShift: "" },
    ];

    shifts.forEach(shift => {
        switch (shift.day) {
            case "MONDAY":
                orderedShifts[0] = shift;
                break;
            case "TUESDAY":
                orderedShifts[1] = shift;
                break;
            case "WEDNESDAY":
                orderedShifts[2] = shift;
                break;
            case "THURSDAY":
                orderedShifts[3] = shift;
                break;
            case "FRIDAY":
                orderedShifts[4] = shift;
                break;
            case "SATURDAY":
                orderedShifts[5] = shift;
                break;
            case "SUNDAY":
                orderedShifts[6] = shift;
                break;
        }
    });

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
        return (
            <section className="section">
                <div className="container">
                    <p>{shiftError}</p>
                </div>
            </section>
        );
    }

    const updateShift = async (staffId, shift) => {
        const draft = shiftDrafts[shift.shiftId] ?? shift;

        updateShiftMutate(
            {
                staffId,
                day: shift.day,
                startShift: draft.startShift,
                endShift: draft.endShift
            },
            {
                onSuccess: () => {
                    setSuccess("Shift updated successfully!");
                    setTimeout(() => setSuccess(""), 3000);
                },
                onError: () => {
                    setError("Failed to update shift.");
                    setTimeout(() => setError(""), 3000);
                }
            }
        );
    }

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

                {success && <p className="form-success">{success}</p>}
                {error && <p className="form-error">{error}</p>}

                <div className="shift-grid">
                    {orderedShifts.map((shift) => {

                        const isModified = isShiftModified(shift);

                        return (
                            <Card
                                key={shift.shiftId}
                                className={`shift-card ${isModified ? "shift-card-modified" : ""}`}
                            >
                                <div className="card-body shift-card-body">
                                    <div className="shift-card-top">
                                        <div>
                                            <p className="shift-label">Working day</p>
                                            <h3 className="shift-day">{shift.day}</h3>
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
                                                onChange={(event) => setShiftDrafts((currentDrafts) => ({
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
                                                onChange={(event) => setShiftDrafts((currentDrafts) => ({
                                                        ...currentDrafts,
                                                        [shift.shiftId]: {
                                                            ...currentDrafts[shift.shiftId],
                                                            endShift: event.target.value
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

                                        <Button
                                            disabled={!isModified}
                                            className="btn btn-primary shift-save-btn"
                                            onClick={() => updateShift(currentStaff?.staffId, shift)}
                                        >
                                            {isModified ? "Save changes" : "Up to date"}
                                        </Button>
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