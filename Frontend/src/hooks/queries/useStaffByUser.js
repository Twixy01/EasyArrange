import { useQuery } from "@tanstack/react-query";
import { staffApi } from "../../api/staffApi";

export function useStaffByUser(userId) {
    return useQuery({
        queryKey: ["staffByUser", userId],
        queryFn: () => staffApi.getStaffByUser(userId),
        enabled: !!userId,
        retry: false,
        refetchOnWindowFocus: false,
        staleTime: 1000 * 60 * 5 // 5 minutes
    })
}