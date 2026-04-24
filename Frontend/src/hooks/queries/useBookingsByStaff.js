import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useBookingsByStaff(staffId) {
  return useQuery({
    queryKey: ["bookings", staffId],
    queryFn: () => salonApi.getBookingsByStaff(staffId),
    enabled: !!staffId,
  });
}