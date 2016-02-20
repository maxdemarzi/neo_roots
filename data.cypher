CREATE INDEX ON :Person(name);
CREATE (u:Person {name:"1-Max"});

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "1-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"2-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "2-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"3-" + names[id% size(names)]+id}) );


WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "3-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"4-" + names[id% size(names)]+id}) );


WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "4-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"5-" + names[id% size(names)]+id}) );


WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "5-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"6-" + names[id% size(names)]+id}) );


WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "6-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"7-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "7-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"8-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "8-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"9-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "9-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"10-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "10-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"11-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "11-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"12-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "12-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"13-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "13-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"14-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "14-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"15-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "15-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"16-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "16-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"17-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "17-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"18-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "18-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"19-" + names[id% size(names)]+id}) );

WITH ["Jennifer","Michelle","Tanya","Julie","Christie","Sophie","Amanda","Khloe","Sarah","Kaylee"] AS names
MATCH (users:Person) WHERE users.name STARTS WITH "19-"
WITH range(1,2) as children, users, names
FOREACH (id in children | CREATE (users)-[:HAS_CHILD]->(:Person {name:"20-" + names[id% size(names)]+id}) );