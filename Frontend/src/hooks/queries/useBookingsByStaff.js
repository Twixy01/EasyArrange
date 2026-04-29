import { useQuery } from "@tanstack/react-query";
import { bookingApi } from "../../api/bookingApi";

export function useBookingsByStaff(staffId) {
  return useQuery({
    queryKey: ["bookings", staffId],
    queryFn: () => bookingApi.getBookingsByStaff(staffId),
    enabled: !!staffId,
  });
}