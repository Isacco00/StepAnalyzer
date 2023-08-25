INSERT INTO public.material (token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo,
                             spessore, trasporto)
VALUES ((select nextval('material_seq')), 14.55, '300/240 LIGHTHERM', '1335X2950', 1.40, 0.00, 'SP.MM.6,00', 1.00);
INSERT INTO public.material (token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo,
                             spessore, trasporto)
VALUES ((select nextval('material_seq')), 18.12, '300/240 LIGHTHERM', '1335X2950', 1.40, 0.00, ' SP.MM.10,00', 1.00);
INSERT INTO public.material (token_material, costo_al_kg, descrizione, dimensioni, peso_specifico, peso_specifico_costo,
                             spessore, trasporto)
VALUES ((select nextval('material_seq')), 10.00, '300/240 LIGHTHERM', '1335X2950', 1.25, 0.00, 'SP.MM.=>12,00', 1.00);
