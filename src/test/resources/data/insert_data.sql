DELETE FROM model;
DELETE FROM brand;
DELETE FROM sub_type;
DELETE FROM vehicle_type;

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

