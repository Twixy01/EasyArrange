import { useQuery } from '@tanstack/react-query'
import { salonApi } from '../../api/salonApi'

export function useUsers() {
    return useQuery({
        queryKey: ['users'],
        queryFn: salonApi.getUsers
    })
}

export default useUsers
