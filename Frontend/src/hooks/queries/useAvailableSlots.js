import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useAvailableSlots(staffId, selectedDate) {
  return useQuery({
    queryKey: ["availableSlots", staffId, selectedDate],
    queryFn: () => salonApi.getAvailableSlots(staffId, selectedDate),
    enabled: !!staffId && !!selectedDate,
  });
}