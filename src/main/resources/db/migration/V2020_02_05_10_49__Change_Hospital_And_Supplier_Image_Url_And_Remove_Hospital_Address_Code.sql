alter table hospital_info
drop column hospital_verify_info_url_1;

alter table hospital_info
drop column hospital_verify_info_url_2;

alter table hospital_info
drop column hospital_verify_info_url_3;

alter table hospital_info
add column hospital_verify_image_urls text;

alter table material_supplier_info
drop column material_supplier_verify_info_url_1;

alter table material_supplier_info
drop column material_supplier_verify_info_url_2;

alter table material_supplier_info
drop column material_supplier_verify_info_url_3;

alter table material_supplier_info
add column material_supplier_verify_image_urls text;

alter table hospital_info
drop column hospital_address_code;