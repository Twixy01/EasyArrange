import { useQuery } from "@tanstack/react-query";
import { salonApi } from "../../api/salonApi";

export function useStaffByUser(userId) {
    return useQuery({
        queryKey: ["staffByUser", userId],
        queryFn: () => salonApi.getStaffByUser(userId),
        enabled: !!userId,
        retry: false,
        refetchOnWindowFocus: false,
        staleTime: 1000 * 60 * 5 // 5 minutes
    })
}