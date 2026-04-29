import { useMutation, useQueryClient } from "@tanstack/react-query";
import { calendarApi } from "../../api/calendarApi";

export function useCreateCalendarBlock() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: calendarApi.createCalendarBlock,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["calendarBlocks"] });
        }
    });
}