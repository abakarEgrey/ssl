BEGIN;
  
-- Table: ssl_information
CREATE TABLE ssl_information
(
  id BIGSERIAL PRIMARY KEY,
  subject character varying(255),
  issuer character varying(255),
  is_valid character varying(255)
 );


ALTER TABLE ssl_information OWNER TO postgres;

GRANT ALL ON TABLE ssl_information TO postgres;

COMMIT;
