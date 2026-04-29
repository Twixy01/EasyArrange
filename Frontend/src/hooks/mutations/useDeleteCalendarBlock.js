import { useMutation, useQueryClient } from "@tanstack/react-query";
import { calendarApi } from "../../api/calendarApi";

export function useDeleteCalendarBlock() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: calendarApi.deleteCalendarBlock,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["calendarBlocks"] });
    }
  });
}
