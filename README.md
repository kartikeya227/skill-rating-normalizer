# Project Setup

- This is a basic Maven - Spring Boot project.
- Find Postman collection along at same level.

## Prerequisites
- Update : LOG_DIR in application-dev.properties to your desired location.

## Running the Project

- Use the following VM arguments: `-Dspring.profiles.active=dev`
- base url: http://localhost:8080
- context path: /api/v1/rating-normalizer
# Graph DB Schema

## Nodes

In the context of a graph database like Neo4j, nodes represent entities in your domain model. Here are the entities that map to nodes:

- **User**: Each user in your system corresponds to a node in the graph database. Each node stores properties such as name, year of experience, and current company.
- **Skill**: Each skill is represented by a node in the graph. These nodes store properties such as the skill name.
- **Domain**: Each domain is a node in the graph, storing properties like the domain name.
- **Review**: Each review instance is also represented by a node. These nodes store properties such as original rating, normalized rating, comment, and years worked together.

## Edges

- **User-Skill Relationship Edge**: Represents the user's proficiency or experience in a particular skill. The weight is the average normalized rating of all reviews for the specific user-skill combination.
- **Skill-Domain Relationship Edge**: Indicates the relationship between a skill and the domain it belongs to. There is no inherent weight associated with this relationship.
- **Review-User Relationship Edge**: Represents the association between a review and the user who gave it (reviewer) or received it (reviewee). The weight is the normalized rating of the review.
- **Review-Skill Relationship Edge**: Indicates the skill that a review is related to. The weight is the normalized rating of the review.

# Neo4J Queries

Access the Neo4J workspace at [https://workspace-preview.neo4j.io/workspace/query](https://workspace-preview.neo4j.io/workspace/query) with the following credentials:

- Username: `neo4j`
- Password: `Z2lRb_fFpUM9O-hCKSyf9SLIvjW58YvUpXRlnSvHNR8`

## Query to see domains and their direct skills

```cypher
MATCH p=()-[:HAS_SKILL|:SUB_SKILL]->() RETURN p LIMIT 25;
```
## Query to see users and their skills

```cypher
MATCH p=()-[:HAS_SKILL]->() RETURN p LIMIT 25;
```
## Query to see users and their reviews

```cypher
MATCH p=()-[:REVIEWED]->() RETURN p LIMIT 25;
```
## Query to see users and their reviews with skills

```cypher
MATCH p=()-[:REVIEWED]->()-[:HAS_SKILL]->() RETURN p LIMIT 25;
```
## Query to see users and their reviews with skills and domains

```cypher
MATCH p=()-[:REVIEWED]->()-[:HAS_SKILL]->()-[:SUB_SKILL|:HAS_SKILL]->() RETURN p LIMIT 25;
```
Made with ❤️ by Kartikeya
