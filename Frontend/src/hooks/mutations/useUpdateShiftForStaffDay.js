import { useMutation, useQueryClient } from "@tanstack/react-query";
import { shiftApi } from "../../api/shiftApi";

export function useUpdateShiftForStaffDay() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (payload) => shiftApi.updateShiftForStaffDay(payload),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["shiftsByStaff"] });
        }
    });
}