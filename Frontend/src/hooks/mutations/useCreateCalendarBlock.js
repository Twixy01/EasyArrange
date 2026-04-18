import { useMutation, useQueryClient } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useCreateCalendarBlock() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: salonApi.createCalendarBlock,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["calendarBlocks"] });
        }
    });
}