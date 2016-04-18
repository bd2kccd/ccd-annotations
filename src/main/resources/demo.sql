-- author: Mark Silvis
-- Demo data

-- Create Accesses
INSERT INTO access (id, name, description) VALUES (1, 'PUBLIC','Visable to all users'), (2, 'GROUP', 'Visible to members of specified group'), (3, 'PRIVATE','Visable only to creator');

-- Create Persons
INSERT INTO person (id, description, email, first_name, last_name, workspace) VALUES (1, 'Software engineer at the University of Pittsburgh', 'marksilvis@pitt.edu', 'Mark', 'Silvis', '~/ccd_workspace'), (2, 'Mathematician and cryptanalyst', 'alan@example.com', 'Alan', 'Turing', '~/ccd_workspace/');

-- Create User Accounts
INSERT INTO user_account (id, activation_key, active, created_date, password, username, person_id) VALUES (1, 'abcd', b'1', '2016-04-18 13:46:24', '$shiro1$SHA-256$500000$FaBYK+qhxdq88RF2elT6Lw==$37trEXXrvblwpjk5kzgQ7bijnxxDIiDol678FYhAC50=', 'mark', 1), (2, 'efgh', b'1', '2016-03-24 15:37:06', '$shiro1$SHA-256$500000$GcAwGj3vh1FHgWldo/Dl1g==$Zx0NxdigCCW5acGMuS6lW50dghHw3trjrssjn70sqtQ=', 'alan', 2);

-- Create User Role
INSERT INTO user_role (id, name, description) VALUES (1, 'USER', 'Standard user'), (2, 'ADMIN', 'Administrator');

-- Create User Role User Account relationships
INSERT INTO user_account_user_role_rel (user_account_id, user_role_id) VALUES (1, 1), (1, 2), (2, 1);   -- Mark is a USER and ADMIN, Alan is a USER

-- Create Groups
INSERT INTO groups (id, name, description) VALUES (1, 'CCD', 'Center for Causal Discovery');

-- Create Group_Membership mappings
INSERT INTO group_membership VALUES (1, 1);     -- User Mark is a member of group 'CCD'

-- Create Group_Moderation mappings
INSERT INTO group_moderation VALUES (1, 1);     -- User Mark is a moderator of group 'CCD'

-- Create Group_Requests mappings
INSERT INTO group_requests VALUES (1, 2);       -- User Alan is requesting access of group 'CCD'

-- Create Uploads
INSERT INTO upload (id, title, created, version, address, user_account_id) VALUES (1, 'Department of Biomedical Informatics', '2016-03-24 15:58:15', 0, 'http://dbmi.pitt.edu', 1), (2, 'Center for Causal Discovery', '2016-04-18 13:23:02', 0, 'http://www.ccd.pitt.edu', 1);

-- Create Vocabularies
INSERT INTO vocabulary (id, description, name, version) VALUES (1, 'Text with no required structure', 'Plaintext', 0), (2, 'Health Care and Life Sciences (https://www.w3.org/wiki/HCLS)', 'HCLS', 0);

-- Create Attributes
INSERT INTO attribute (id, level, name, requirement_level, vocab_id) VALUES (1, NULL, 'Text', NULL, 1);     -- Plaintext attributes
INSERT INTO attribute (id, level, name, requirement_level, vocab_id) VALUES (2, 'Summary', 'Type', 'Required', 2), (3, 'Summary', 'Title', 'Required', 2), (4, 'Summary', 'Description', 'Required', 2);    -- HCLS attributes

-- Create Annotations with Annotation Data
-- Text annotation on DBMI with Public access control
INSERT INTO annotation (id, redacted, version, access_control, parent_id, upload_id, user_account_id, vocab_id) VALUES (1, b'0', 0, 1, NULL, 1, 1, 1), (2, b'0', 0, 1, 1, 1, 1, 1);
INSERT INTO annotation_data (id, value, annotation_id, attribute_id) VALUES (1, 'Public annotation', 1, 1), (2, 'Public annotation one level down', 2, 1);

-- HCLS annotation on DBMI with Public access control
INSERT INTO annotation (id, redacted, version, access_control, parent_id, upload_id, user_account_id, vocab_id) VALUES (3, b'0', 0, 1, NULL, 1, 1, 2);
INSERT INTO annotation_data (id, value, annotation_id, attribute_id) VALUES (3, 'HCLS description metadata', 3, 2);

-- Text annotation on CCD with Public access control
INSERT INTO annotation (id, redacted, version, access_control, parent_id, upload_id, user_account_id, vocab_id) VALUES (4, b'0', 0, 1, NULL, 2, 1, 1);
INSERT INTO annotation_data (id, value, annotation_id, attribute_id) VALUES (4, 'This is the center.', 4, 1);

-- Text annotation on DBMI with Group access control
INSERT INTO annotation (id, redacted, version, access_control, group_id, parent_id, upload_id, user_account_id, vocab_id) VALUES (5, b'0', 0, 2, 1, NULL, 1, 1, 1);
INSERT INTO annotation_data (id, value, annotation_id, attribute_id) VALUES(5, 'Group annotation', 5, 1);

-- Text annotation on DBMI with Private access control
INSERT INTO annotation (id, redacted, version, access_control, parent_id, upload_id, user_account_id, vocab_id) VALUES (6, b'0', 0, 3, NULL, 1, 1, 1);
INSERT INTO annotation_data (id, value, annotation_id, attribute_id) VALUES (6, 'Private annotation', 6, 1), (7, 'Private annotation with additional data', 6, 1);
