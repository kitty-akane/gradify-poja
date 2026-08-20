CREATE TABLE IF NOT EXISTS group_hei (
    id            UUID PRIMARY KEY,
    ref           VARCHAR(255),
    level         VARCHAR(50)
                  CHECK (level IN ('L1','L2','L3')),
    academic_year VARCHAR(255)
);
