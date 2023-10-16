CREATE TRIGGER entry_update_trigger
AFTER UPDATE ON cvs
FOR EACH ROW
EXECUTE FUNCTION check_and_notify();
