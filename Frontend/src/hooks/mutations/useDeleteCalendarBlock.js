import { useMutation, useQueryClient } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useDeleteCalendarBlock() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: salonApi.deleteCalendarBlock,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["calendarBlocks"] });
    }
  });
}
