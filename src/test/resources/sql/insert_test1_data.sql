INSERT INTO public.booking(
	id, user_id, vehicle_id, booked_from_date, booked_to_date, booking_status, city_start, city_end, create_date)
	values
	(2, 1, 1, '2024-11-09', '2024-11-13', 'ACTIVE', 'BERLIN', 'BERLIN', '2024-11-08 12:00:00.000000'),
	(3, 1, 2, '2024-11-15', '2024-11-13', 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000'),
	(4, 1, 3, '2024-11-19', '2024-11-22', 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000'),
	(5, 1, 5, '2024-11-18', '2024-11-19', 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000'),
	(6, 1, 6, '2024-11-08', '2024-11-11', 'FINISHED', 'BERLIN', 'BERLIN', '2024-11-07 12:00:00.000000'),
	(7, 1, 7, '2024-11-12', '2024-11-20', 'ACTIVE', 'BERLIN', 'BERLIN', '2024-11-07 12:00:00.000000'),
	(8, 1, 8, '2024-11-15', '2024-11-16', 'CANCELLED', 'BERLIN', 'BERLIN', '2024-11-07 12:00:00.000000');


