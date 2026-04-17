import { useMutation, useQueryClient } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useDeleteStaffShift() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: salonApi.deleteStaffShift,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["shiftsByStaff"] });
        }
    });
}