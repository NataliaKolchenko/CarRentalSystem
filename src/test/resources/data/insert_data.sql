DELETE FROM booking;
DELETE FROM vehicle;
DELETE FROM model;
DELETE FROM brand;
DELETE FROM sub_type;
DELETE FROM vehicle_type;
DELETE FROM branch;
DELETE FROM address;

INSERT INTO public.brand(
	id, brand_name)
	VALUES
	(100, 'Ford'),
	(101, 'Audi');

INSERT INTO public.model(
	id, model_name, brand_id)
	VALUES
	(100, 'Mustang', 100),
	(101, 'GT', 100);

INSERT INTO public.vehicle_type(
	id, vehicle_type_name)
	VALUES
	(100, 'Auto'),
	(101, 'Bus');

INSERT INTO public.sub_type(
	id, sub_type_name, vehicle_type_id)
	VALUES
	(100, 'Lux', 100),
	(101, 'Normal', 100);

INSERT INTO public.address(
	id, zip_code, country, region, city, district, street, house, apartment, additional_info, create_date)
	VALUES (100, '14000', 'Germany', 'BERLIN', 'BERLIN', 'Main district', 'Mainstraße', 2,
	'GMbH CarRentalSystem', '2 OG', '2024-11-12 12:00:00.000000'),
	(101, '14000', 'Germany', 'BERLIN', 'BERLIN', 'Main district', 'Mainstraße', 3,
    	'GMbH CarRentalSystem', '2 OG', '2024-11-12 12:00:00.000000');

INSERT INTO public.branch(
	id, branch_name, address_id, working_time, phone)
	VALUES (100, 'Berlin Brunch', 100, 'MO - FR: 9:00 - 18:00', '+496872211'),
	(101, 'Berlin Brunch', 101, 'MO - FR: 9:00 - 18:00', '+496872211');

INSERT INTO public.vehicle(
	id, vehicle_type_id, sub_type_id, active, brand_id, model_id, engine_type, year, branch_id, transmission_type, mileage,
	city, favorite, vin_code, vehicle_number, create_date, update_date)
	VALUES
	(100, 100, 100, true, 100, 100, 'PETROL', 1999, 100, 'MANUAL', 230445,
	'BERLIN', true, 'AAA123', 'SSS555', '2024-11-12 12:00:00.000000', null),
	(101, 100, 100, true, 100, 100, 'PETROL', 1998, 100, 'MANUAL', 230440,
    	'BERLIN', false, 'BBB111', 'UUU444', '2024-11-12 12:00:00.000000', null),
    (102, 100, 100, true, 100, 100, 'PETROL', 1998, 100, 'MANUAL', 230440,
        	'BERLIN', false, 'YYY555', 'HHH664', '2024-11-12 12:00:00.000000', null),
    (103, 100, 100, true, 100, 100, 'PETROL', 1998, 100, 'MANUAL', 230440,
        	'BERLIN', false, 'TTT434', 'EEE333', '2024-11-12 12:00:00.000000', null);

INSERT INTO public.booking(
	id, user_id, vehicle_id, booked_from_date, booked_to_date, booking_status, city_start, city_end, create_date, update_date)
	VALUES
	(100, 'user@user.user', 100, '2024-12-05', '2024-12-05', 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
--	 id 101 - for cancel
	(101, 'user@user.user', 100, '2024-12-06', '2024-12-06', 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
--	id 102 - for finish
	(102, 'user@user.user', 101, '2024-11-20', CURRENT_DATE, 'ACTIVE', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
--	id 103 - for activate
	(103, 'user@user.user', 101, CURRENT_DATE, CURRENT_DATE, 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
	(104, 'real_admin@real.admin', 101, CURRENT_DATE, CURRENT_DATE, 'CREATED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
	(105, 'user@user.user', 101, '2024-11-11', '2024-11-20', 'FINISHED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
	(106, 'user@user.user', 100, '2024-11-11', '2024-11-20', 'CANCELLED', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null),
	(107, 'user@user.user', 101, '2024-11-20', '2024-11-30', 'ACTIVE', 'BERLIN', 'BERLIN', '2024-11-12 12:00:00.000000', null);


