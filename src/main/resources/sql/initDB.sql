DROP TABLE IF EXISTS public.courses;

CREATE TABLE IF NOT EXISTS public.courses
(
    id                 INT              NOT NULL,
    target_currency    VARCHAR(3)       NOT NULL,
    available_currency VARCHAR(3)       NOT NULL,
    course             DOUBLE PRECISION NOT NULL,
    CONSTRAINT courses_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.courses
    OWNER to postgres;