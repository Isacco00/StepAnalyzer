INSERT INTO public.material
(token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo, spessore, trasporto)
VALUES((select nextval('material_seq')), 12.28, '300/240 LIGHTHERM', '1335X2950', 1.4, 0.000017192, 'SP.MM.=>12,00', 1);

INSERT INTO public.material
(token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo, spessore, trasporto)
VALUES((select nextval('material_seq')), 14.55, '300/240 LIGHTHERM', '1335X2950', 1.4, 0.000020370, 'SP.MM.6,00', 1);

INSERT INTO public.material
(token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo, spessore, trasporto)
VALUES((select nextval('material_seq')), 18.12, '300/240 LIGHTHERM', '1335X2950', 1.4, 0.000025368, ' SP.MM.10,00', 1);