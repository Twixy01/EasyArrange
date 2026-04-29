import { useQuery } from '@tanstack/react-query'
import { bookingApi } from '../../api/bookingApi'

export function useOverlappingBookings(staffId, startDateTime, endDateTime) {
  return useQuery({
    queryKey: ['overlappingBookings', staffId, startDateTime, endDateTime],
    queryFn: () => bookingApi.getOverlappingBookings(staffId, startDateTime, endDateTime),
    enabled: !!staffId && !!startDateTime && !!endDateTime,
    retry: false,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
  })
}