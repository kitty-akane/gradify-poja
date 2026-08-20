CREATE TABLE IF NOT EXISTS teacher (
    id      UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE
             REFERENCES user_hei(id)
);

CREATE TABLE IF NOT EXISTS student (
    id             UUID PRIMARY KEY,
    entry_date     DATE,
    student_number VARCHAR(255) NOT NULL UNIQUE,
    user_hei_id    UUID NOT NULL UNIQUE
                    REFERENCES user_hei(id)
);

CREATE TABLE IF NOT EXISTS course_offering (
    id            UUID PRIMARY KEY,
    academic_year VARCHAR(255) NOT NULL,
    track         VARCHAR(255),
    course_id     UUID NOT NULL REFERENCES course(id),
    group_id      UUID NOT NULL REFERENCES group_hei(id),
    CONSTRAINT ukccvlaku4o7haxo9svu4q6wilb UNIQUE (course_id, group_id, academic_year)
);

CREATE TABLE IF NOT EXISTS course_offering_teacher (
    course_offering_id UUID NOT NULL REFERENCES course_offering(id),
    teacher_id          UUID NOT NULL REFERENCES teacher(id)
);

CREATE TABLE IF NOT EXISTS enrollment (
    id            UUID PRIMARY KEY,
    academic_year VARCHAR(255),
    level         VARCHAR(255)
                  CHECK (level IN ('L1','L2','L3')),
    track         VARCHAR(255)
                  CHECK (track IN ('TRONC_COMMUN','TN','EL')),
    group_id      UUID NOT NULL REFERENCES group_hei(id),
    student_id    UUID NOT NULL REFERENCES student(id),
    CONSTRAINT uk9v5pqcfm0ybnk7lyrj6nkx2i6 UNIQUE (student_id, academic_year),
    CONSTRAINT uk_enrollment_student_year_group UNIQUE (student_id, academic_year, group_id)
);

CREATE TABLE IF NOT EXISTS exam (
    id           UUID PRIMARY KEY,
    coefficient  NUMERIC(38, 2),
    exam_date    TIMESTAMP WITH TIME ZONE,
    label        VARCHAR(255),
    offering_id  UUID NOT NULL REFERENCES course_offering(id)
);

CREATE TABLE IF NOT EXISTS grade (
    id         UUID PRIMARY KEY,
    value      NUMERIC(38, 2),
    exam_id    UUID NOT NULL REFERENCES exam(id),
    student_id UUID NOT NULL REFERENCES student(id),
    CONSTRAINT uk15agkvnurttru8fa7vkb1v353 UNIQUE (exam_id, student_id)
);

CREATE TABLE IF NOT EXISTS grade_history (
    id             UUID PRIMARY KEY,
    modified_at    TIMESTAMP WITH TIME ZONE,
    new_value      NUMERIC(38, 2),
    old_value      NUMERIC(38, 2),
    reason         VARCHAR(500) NOT NULL,
    grade_id       UUID NOT NULL REFERENCES grade(id),
    modified_by_id UUID NOT NULL REFERENCES user_hei(id)
);
