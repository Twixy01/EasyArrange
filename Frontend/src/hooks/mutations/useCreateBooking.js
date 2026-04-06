import { useMutation, useQueryClient } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useCreateBooking() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: salonApi.createBooking,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["availableSlots"] });
        }
    });
}