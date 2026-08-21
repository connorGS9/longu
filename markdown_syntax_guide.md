# The Markdown Syntax Guide

A walk through every common Markdown feature: the raw syntax on the left, what it
does on the right. Where a feature is worth seeing rendered, the raw version is
shown in a code block and then the live version right below it.

> [!NOTE]
> Markdown has several "flavors." Plain **CommonMark** is the base. **GitHub
> Flavored Markdown (GFM)** adds tables, task lists, strikethrough, autolinks,
> and alert boxes. A few features here (alerts, footnotes, collapsible sections)
> only render on GitHub and some other viewers, and are flagged where they come
> up.

---

## Table of contents

- [Headings](#headings)
- [Paragraphs and line breaks](#paragraphs-and-line-breaks)
- [Emphasis](#emphasis)
- [Blockquotes](#blockquotes)
- [Lists](#lists)
- [Code](#code)
- [Horizontal rules](#horizontal-rules)
- [Links](#links)
- [Images](#images)
- [Tables](#tables)
- [Escaping characters](#escaping-characters)
- [HTML inside Markdown](#html-inside-markdown)
- [Collapsible sections](#collapsible-sections)
- [Alerts and callouts](#alerts-and-callouts)
- [Footnotes](#footnotes)
- [Emoji](#emoji)
- [Comments](#comments)
- [Building a table of contents](#building-a-table-of-contents)

> The links above jump to the matching headings. That is how a table of contents
> works: a link to `#some-heading` scrolls to that heading. See
> [Building a table of contents](#building-a-table-of-contents) for the rules.

---

## Headings

Put one to six `#` characters at the start of a line. More `#` means a smaller
heading.

```markdown
# H1, the page title
## H2, a major section
### H3, a subsection
#### H4
##### H5
###### H6
```

Leave a blank line before and after a heading so it renders cleanly. There is an
older underline style (`===` under a line for H1, `---` for H2), but the `#`
style is clearer and used almost everywhere.

---

## Paragraphs and line breaks

A paragraph is just text with a blank line above and below it. This trips people
up: pressing Enter once does **not** make a new line in the output. You need
either a blank line (new paragraph) or two trailing spaces (soft line break).

```markdown
This is one paragraph. Even though this sentence
is on a new line in the source, it renders on the same line.

This is a second paragraph, because of the blank line above it.

End a line with two spaces to force a break,  
and this text lands on the next line without a full paragraph gap.
```

A cleaner way to force a break without invisible trailing spaces is a backslash
at the end of the line:

```markdown
First line\
Second line
```

---

## Emphasis

```markdown
*italic* or _italic_
**bold** or __bold__
***bold italic***
~~strikethrough~~
```

Renders as: *italic*, **bold**, ***bold italic***, ~~strikethrough~~.

> [!TIP]
> Prefer `*` for italic and `**` for bold and stay consistent. The underscore
> versions can misfire in the middle of a word (like `some_variable_name`), so
> asterisks are the safer default. Strikethrough (`~~`) is a GFM feature.

---

## Blockquotes

Start a line with `>`. Quotes can nest and can contain other Markdown.

```markdown
> This is a quote.
>
> > This is a nested quote.
>
> Quotes can contain **bold**, lists, and `code`.
```

Renders as:

> This is a quote.
>
> > This is a nested quote.
>
> Quotes can contain **bold**, lists, and `code`.

---

## Lists

### Unordered

Use `-`, `*`, or `+`. Pick one and stay consistent. Indent to nest.

```markdown
- First item
- Second item
  - Nested item (indent two or four spaces)
  - Another nested item
- Third item
```

Renders as:

- First item
- Second item
  - Nested item (indent two or four spaces)
  - Another nested item
- Third item

### Ordered

Use numbers followed by a period. The actual numbers do not have to be correct,
the renderer counts for you, but writing them in order keeps the source readable.

```markdown
1. First
2. Second
3. Third
```

### Task lists (GFM)

Checkboxes. `[ ]` is unchecked, `[x]` is checked.

```markdown
- [x] Write the scanner
- [x] Write the parser
- [ ] Write the resolver
- [ ] Write the interpreter
```

Renders as:

- [x] Write the scanner
- [x] Write the parser
- [ ] Write the resolver
- [ ] Write the interpreter

---

## Code

### Inline code

Wrap a snippet in single backticks: `` `like this` ``. Use it for variable
names, filenames, and short commands, such as `malloc`, `dll.c`, or
`gcc -Wall`.

### Fenced code blocks

Wrap a block in triple backticks. Put a language name after the opening fence to
turn on syntax highlighting.

````markdown
```c
Node* find(List* ll, const char* value) {
    for (Node* cur = ll->head; cur != NULL; cur = cur->next) {
        if (strcmp(cur->value, value) == 0) return cur;
    }
    return NULL;
}
```
````

Renders as:

```c
Node* find(List* ll, const char* value) {
    for (Node* cur = ll->head; cur != NULL; cur = cur->next) {
        if (strcmp(cur->value, value) == 0) return cur;
    }
    return NULL;
}
```

> [!NOTE]
> To display a fenced code block *inside* another one (as above), wrap the outer
> one in **four** backticks. The rule is: the outer fence needs more backticks
> than any fence inside it.

---

## Horizontal rules

Three or more `-`, `*`, or `_` on their own line. Put a blank line above so it is
not mistaken for a heading underline.

```markdown
---
```

Renders as a full-width divider line, like the ones separating sections here.

---

## Links

```markdown
Inline link: [visible text](https://example.com)
Link with a tooltip: [text](https://example.com "hover title")
Bare URL (GFM autolinks it): https://example.com
Reference link: [visible text][ref] ... then later ... [ref]: https://example.com
Link to a heading in this file: [jump](#headings)
```

Renders as: [visible text](https://example.com), and a jump to
[Headings](#headings).

Reference-style links keep long paragraphs readable by moving the URL out of the
sentence:

```markdown
See the [Rust compiler][rustc] for a real example.

[rustc]: https://github.com/rust-lang/rust
```

---

## Images

Same as a link, but with a leading `!`. The text in brackets is the alt text
(shown if the image fails to load, and read by screen readers).

```markdown
![alt text describing the image](path/or/url.png)
![alt text](image.png "optional hover title")
```

To make an image clickable, wrap it in a link:

```markdown
[![alt text](image.png)](https://example.com)
```

---

## Tables (GFM)

Columns separated by `|`. The second row of dashes defines the header boundary
and the alignment. A colon marks alignment: left, right, or centered.

```markdown
| Left | Center | Right |
|:-----|:------:|------:|
| a    | b      | c     |
| long | short  | 42    |
```

Renders as:

| Left | Center | Right |
|:-----|:------:|------:|
| a    | b      | c     |
| long | short  | 42    |

> [!TIP]
> The source columns do not need to line up perfectly, the renderer sorts it
> out. But lining them up makes the raw file much easier to read and edit.

---

## Escaping characters

To show a Markdown character literally instead of letting it format, put a
backslash in front of it.

```markdown
\*not italic\*
\# not a heading
\`not code\`
```

Renders as: \*not italic\*, \# not a heading, \`not code\`.

Characters you may need to escape: `` \ ` * _ { } [ ] ( ) # + - . ! | ``

---

## HTML inside Markdown

Most Markdown renderers let you drop in raw HTML for things Markdown cannot do on
its own, like centering, setting an image width, or superscripts.

```markdown
<p align="center">Centered text</p>
<img src="logo.png" width="200" alt="logo">
E = mc<sup>2</sup> and a chemical formula H<sub>2</sub>O
```

> [!WARNING]
> HTML support varies by renderer. GitHub allows a safe subset. Some strict
> CommonMark viewers strip it. If a file must render identically everywhere,
> lean on plain Markdown and use HTML only when you need it.

---

## Collapsible sections

A GitHub feature using HTML `<details>` and `<summary>`. The content stays hidden
until the reader clicks the summary line. Good for long examples or optional
detail.

```markdown
<details>
<summary>Click to expand the full example</summary>

Hidden content goes here. It can contain any Markdown:

- lists
- `code`
- **bold text**

</details>
```

Renders as a clickable "Click to expand the full example" line that reveals the
content when clicked. Leave a blank line after `</summary>` or the inner Markdown
may not render.

---

## Alerts and callouts

A GitHub feature. A blockquote whose first line is one of five keywords in the
`[!KEYWORD]` form becomes a colored callout box.

```markdown
> [!NOTE]
> Useful information the reader should notice.

> [!TIP]
> A helpful suggestion.

> [!IMPORTANT]
> Something essential to success.

> [!WARNING]
> Something that needs careful attention.

> [!CAUTION]
> A risk or negative consequence.
```

On GitHub these render as tinted boxes with an icon. On viewers that do not
support them, they degrade gracefully into ordinary blockquotes, so they are
safe to use either way. You have seen them throughout this file.

---

## Footnotes

A GFM feature. Mark a spot with `[^id]`, then define the note anywhere in the
file. The renderer collects them at the bottom and links back and forth.

```markdown
Here is a claim that needs a source.[^1]

[^1]: This is the footnote text, usually placed at the end of the document.
```

Renders with a small superscript number that links to the note at the bottom of
the page.

---

## Emoji

On GitHub and many chat tools you can type `:shortcode:` names.

```markdown
:rocket: :white_check_mark: :warning: :bug: :tada:
```

You can also paste real Unicode emoji directly into the text, which works
everywhere.

---

## Comments

Markdown has no official comment syntax, but there are two ways to hide text from
the rendered output while keeping it in the source.

```markdown
<!-- This is an HTML comment. It will not appear in the rendered file. -->
```

The HTML comment is the reliable one and works in almost every renderer.

---

## Building a table of contents

There is no automatic TOC in plain Markdown, you build it from links to your own
headings. The trick is knowing the anchor a heading generates:

1. Take the heading text.
2. Lowercase it.
3. Replace spaces with hyphens.
4. Remove characters that are not letters, numbers, or hyphens.

So a heading `## Code Blocks & Fences` becomes the anchor `#code-blocks--fences`
(the `&` is dropped, and the two spaces around it each become a hyphen, giving a
double hyphen).

```markdown
## Table of contents

- [Headings](#headings)
- [Code](#code)
- [Building a table of contents](#building-a-table-of-contents)
```

> [!TIP]
> Most editors (including VS Code with a Markdown extension) can generate and
> update a TOC for you, which avoids getting the anchors wrong by hand.

---

## Quick reference card

| Feature | Syntax |
|:--------|:-------|
| Heading | `# H1` to `###### H6` |
| Bold | `**text**` |
| Italic | `*text*` |
| Strikethrough | `~~text~~` |
| Inline code | `` `code` `` |
| Code block | triple backticks, plus a language name |
| Link | `[text](url)` |
| Image | `![alt](url)` |
| Blockquote | `> text` |
| Unordered list | `- item` |
| Ordered list | `1. item` |
| Task list | `- [ ] todo` and `- [x] done` |
| Table | `\| a \| b \|` with a `\|---\|---\|` divider row |
| Horizontal rule | `---` on its own line |
| Escape a character | `\*` |
| Comment | `<!-- hidden -->` |

That covers the full set you will use day to day. Bookmark the quick reference
card at the bottom, and reach for the sections above when you need the exact
details.
