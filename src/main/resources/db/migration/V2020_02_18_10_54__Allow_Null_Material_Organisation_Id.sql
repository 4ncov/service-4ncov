alter table material_required
change column material_required_organization_id material_required_organization_id bigint unsigned default null;

alter table material_supplied
change column material_supplier_organization_id material_supplier_organization_id bigint unsigned default null;