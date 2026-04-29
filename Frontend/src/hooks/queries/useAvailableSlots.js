import { useQuery } from "@tanstack/react-query";
import { bookingApi } from "../../api/bookingApi";

export function useAvailableSlots(staffId, selectedDate, serviceId) {
  return useQuery({
    queryKey: ["availableSlots", staffId, selectedDate, serviceId],
    queryFn: () => bookingApi.getAvailableSlots(staffId, selectedDate, serviceId),
    enabled: !!staffId && !!selectedDate && !!serviceId,
  });
}