# CSC8019

# Contributing & Git Workflow

**Main branch is PROTECTED.** Direct pushes to `main` are **blocked** by GitHub settings. All changes require pull requests.

## Branch Structure

- `main`  – production (protected, deploys to prod)
- `develop` – integration / staging (default for features)

---

## 1. Cloning the Repo

```bash
git clone <REPO_URL>
cd <REPO_NAME>
git checkout develop
```

##  Workflow

start from development do not push to main 
```bash

git checkout development
git pull origin development
```


create feature branch when adding anew feature to the app
```bash
git checkout -b feature/<short-description>
# e.g.: git checkout -b feature/user-auth


```

Always commit with description




