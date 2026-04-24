import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useBookingsByUser(userId) {
  return useQuery({
    queryKey: ["bookings", userId],
    queryFn: () => salonApi.getBookingsByUser(userId),
    enabled: !!userId,
  });
}
