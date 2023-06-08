ALTER TABLE IF EXISTS public.tbl_post
ADD COLUMN is_active boolean NOT NULL DEFAULT True;