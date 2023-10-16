CREATE OR REPLACE FUNCTION check_and_notify()
RETURNS TRIGGER AS $$
DECLARE
  last_update_time TIMESTAMP;
  time_current TIMESTAMP;
  update_threshold INTERVAL = '15 seconds';  -- Set your threshold here (e.g., '90 days')
BEGIN
  -- Get the timestamp of the last update on the data entry
  last_update_time := NEW.last_updated;  -- Assuming 'updated_at' field

  -- Get the current time
  time_current := NOW();

  -- Calculate the elapsed time since the last update
  IF time_current - last_update_time > update_threshold THEN
    -- If the elapsed time exceeds the threshold, send an email
    -- Replace the placeholders with actual email-sending code.
    -- Example (using plsmtp):
    
    SELECT smtp_sendmail(
      'smtp.example.com',
      587,
      'sjuber@live.dk',
      'amasondude@gmail.com',
      'Subject: Entry Not Updated\n\nThe data entry has not been updated in a long time.'
    );
    
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;