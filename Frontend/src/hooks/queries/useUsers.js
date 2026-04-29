import { useQuery } from '@tanstack/react-query'
import { userApi } from '../../api/userApi'

export function useUsers() {
    return useQuery({
        queryKey: ['users'],
        queryFn: userApi.getUsers
    })
}

export default useUsers
