import { useMutation, useQueryClient } from "@tanstack/react-query";
import { bookingApi } from "../../api/bookingApi";

export function useCreateBooking() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: bookingApi.createBooking,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["availableSlots"] });
        }
    });
}