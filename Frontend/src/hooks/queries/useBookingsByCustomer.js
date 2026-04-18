import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useBookingsByCustomer(customerId) {
  return useQuery({
    queryKey: ["bookings", customerId],
    queryFn: () => salonApi.getBookingsByCustomer(customerId),
    enabled: !!customerId,
  });
}