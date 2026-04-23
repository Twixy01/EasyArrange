import { useMutation } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useUpdateBookingStatus() {
  return useMutation({
    mutationFn: ({ bookingId, bookingUpdateBody }) =>
      salonApi.updateBookingStatus(bookingId, bookingUpdateBody),
  });
}
