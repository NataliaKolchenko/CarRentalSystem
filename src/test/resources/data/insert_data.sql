DELETE FROM model;
DELETE FROM brand;

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