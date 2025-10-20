CREATE TABLE IF NOT EXISTS user_prefs (
  id BIGSERIAL PRIMARY KEY,
  user_id VARCHAR(64) NOT NULL UNIQUE,
  dark_mode BOOLEAN,
  show_online_status BOOLEAN,
  push_notifications BOOLEAN,
  show_info BOOLEAN,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_user_prefs_user_id ON user_prefs(user_id);
