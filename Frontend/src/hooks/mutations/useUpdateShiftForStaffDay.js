import { useMutation, useQueryClient } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useUpdateShiftForStaffDay() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (payload) => salonApi.updateShiftForStaffDay(payload),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["shiftsByStaff"] });
        }
    });
}