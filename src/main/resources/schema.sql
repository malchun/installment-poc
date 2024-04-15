CREATE TABLE IF NOT EXISTS charge (
  id VARCHAR(100) PRIMARY KEY,
  amount INTEGER,
  parent_workflow_id VARCHAR(100),
  charge_index INTEGER,
  transaction_id VARCHAR(100),
  refund_workflow_id VARCHAR(100)
);