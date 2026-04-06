import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useAvailableSlots(staffId, selectedDate, serviceId) {
  return useQuery({
    queryKey: ["availableSlots", staffId, selectedDate, serviceId],
    queryFn: () => salonApi.getAvailableSlots(staffId, selectedDate, serviceId),
    enabled: !!staffId && !!selectedDate && !!serviceId,
  });
}