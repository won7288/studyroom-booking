CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE reservation
ADD CONSTRAINT reservation_no_overlap
EXCLUDE USING gist (
    room_id WITH =,
    tstzrange(start_at, end_at) WITH &&
);