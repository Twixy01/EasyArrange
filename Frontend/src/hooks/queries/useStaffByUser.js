import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useStaffByUser(userId) {
    return useQuery({
        queryKey: ["staffByUser", userId],
        queryFn: () => salonApi.getStaffByUser(userId),
        enabled: !!userId,
    })
}