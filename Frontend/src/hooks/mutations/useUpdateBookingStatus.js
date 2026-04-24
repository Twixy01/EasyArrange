import { useMutation } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useUpdateBookingStatus() {
  return useMutation({
    mutationFn: ({ bookingId, bookingUpdateBody, isStaff }) =>
      salonApi.updateBookingStatus(bookingId, bookingUpdateBody, isStaff),
  });
}
