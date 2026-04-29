import { useMutation, useQueryClient } from "@tanstack/react-query";
import { shiftApi } from "../../api/shiftApi";

export function useDeleteStaffShift() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: shiftApi.deleteStaffShift,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["shiftsByStaff"] });
        }
    });
}