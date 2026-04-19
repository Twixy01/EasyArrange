import { useQuery } from '@tanstack/react-query'
import { salonApi } from '../../api/salonApi'

export function useOverlappingBookings(staffId, startDateTime, endDateTime) {
  return useQuery({
    queryKey: ['overlappingBookings', staffId, startDateTime, endDateTime],
    queryFn: () => salonApi.getOverlappingBookings(staffId, startDateTime, endDateTime),
    enabled: !!staffId && !!startDateTime && !!endDateTime
  })
}