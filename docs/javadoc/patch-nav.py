#!/usr/bin/env python3
"""
Post-processes the generated JavaDoc HTML files to make the
"Package", "Class" and "Use" navigation buttons clickable.

JavaDoc by default leaves them as plain <li> text on pages where they
are not the current context. This script rewrites them as <a> links
pointing to the most useful destination given the current file:

  - Package -> the package-summary.html of the current package, falling
    back to allpackages-index.html when there is no current package
    (e.g. on the overview page).
  - Class   -> the canonical class page (when on a class-related page)
    or allclasses-index.html otherwise.
  - Use     -> class-use/<ClassName>.html for class pages, or
    package-use.html for package pages, or allclasses-index.html as a
    fallback.

The script only touches the three target <li> entries; everything else
is left untouched.
"""

import os
import re
import sys
from pathlib import Path

DOCS_ROOT = Path(__file__).resolve().parent

# Patterns used to identify the three target <li> entries that JavaDoc
# leaves as plain text. The exact match strings below come from JDK 21's
# JavaDoc output.
LI_PATTERNS = {
    'Package': re.compile(r'<li>Package</li>'),
    'Class':   re.compile(r'<li>Class</li>'),
    'Use':     re.compile(r'<li>Use</li>'),
}


def find_package_root(html_path: Path):
    """Return the package directory for the given HTML file, or None.

    A package directory is one that directly contains a
    `package-summary.html`. We walk upward from the file's location.
    """
    for ancestor in html_path.parent.parents:
        if (ancestor / "package-summary.html").exists():
            return ancestor
    if (html_path.parent / "package-summary.html").exists():
        return html_path.parent
    return None


def relpath(target: Path, html_path: Path) -> str:
    """Path of `target` relative to the directory of `html_path`."""
    return os.path.relpath(target, html_path.parent).replace(os.sep, '/')


def make_link(label: str, href: str) -> str:
    return f'<li><a href="{href}" class="nav-link">{label}</a></li>'


def patch_html(html_path: Path) -> bool:
    """Patch one HTML file. Returns True if any change was made."""
    try:
        with open(html_path, encoding='utf-8') as f:
            content = f.read()
    except (UnicodeDecodeError, OSError):
        return False

    if not any(p.search(content) for p in LI_PATTERNS.values()):
        return False

    name = html_path.name
    is_class_use_page = html_path.parent.name == 'class-use'
    pkg_dir = find_package_root(html_path)

    # ----- Package link -----
    if pkg_dir is not None:
        pkg_summary = pkg_dir / "package-summary.html"
        package_href = relpath(pkg_summary, html_path)
    else:
        package_href = relpath(DOCS_ROOT / "allpackages-index.html", html_path)

    # ----- Class link -----
    class_href = None
    if (pkg_dir is not None
            and not is_class_use_page
            and name not in {"package-summary.html", "package-tree.html",
                             "package-use.html", "package-frame.html"}
            and name.endswith(".html")
            and not name.startswith("package-")
            and not name.startswith("class-use")):
        # We are on a class page: link "Class" to this very file.
        class_href = name
    if class_href is None:
        class_href = relpath(DOCS_ROOT / "allclasses-index.html", html_path)

    # ----- Use link -----
    use_href = None
    if (pkg_dir is not None and not is_class_use_page
            and name.endswith(".html")
            and not name.startswith("package-")):
        use_candidate = html_path.parent / "class-use" / name
        if use_candidate.exists():
            use_href = relpath(use_candidate, html_path)
    if use_href is None and pkg_dir is not None:
        pkg_use = pkg_dir / "package-use.html"
        if pkg_use.exists():
            use_href = relpath(pkg_use, html_path)
    if use_href is None:
        use_href = relpath(DOCS_ROOT / "allclasses-index.html", html_path)

    new_content = content
    new_content = LI_PATTERNS['Package'].sub(make_link('Package', package_href), new_content)
    new_content = LI_PATTERNS['Class'].sub(make_link('Class', class_href), new_content)
    new_content = LI_PATTERNS['Use'].sub(make_link('Use', use_href), new_content)

    if new_content == content:
        return False

    with open(html_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    return True


def main() -> int:
    if not (DOCS_ROOT / "index.html").exists():
        print(f"docs root not recognised: {DOCS_ROOT}", file=sys.stderr)
        return 1

    changed = 0
    total = 0
    for path in DOCS_ROOT.rglob("*.html"):
        total += 1
        if patch_html(path):
            changed += 1

    print(f"Processed {total} HTML files; modified {changed}")
    return 0


if __name__ == '__main__':
    sys.exit(main())
