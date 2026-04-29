import { useQuery } from "@tanstack/react-query";
import { bookingApi } from "../../api/bookingApi";

export function useBookingsByUser(userId) {
  return useQuery({
    queryKey: ["bookings", userId],
    queryFn: () => bookingApi.getBookingsByUser(userId),
    enabled: !!userId,
  });
}
