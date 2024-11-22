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
	'GMbH CarRentalSystem', '2 OG', '2024-11-12 12:00:00.000000');

INSERT INTO public.branch(
	id, branch_name, address_id, working_time, phone)
	VALUES (100, 'Berlin Brunch', 100, 'MO - FR: 9:00 - 18:00', '+496872211');

