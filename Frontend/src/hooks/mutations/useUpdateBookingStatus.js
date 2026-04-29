import { useMutation } from "@tanstack/react-query";
import { bookingApi } from "../../api/bookingApi";

export function useUpdateBookingStatus() {
  return useMutation({
    mutationFn: ({ bookingId, bookingUpdateBody, isStaff }) =>
      bookingApi.updateBookingStatus(bookingId, bookingUpdateBody, isStaff),
  });
}
